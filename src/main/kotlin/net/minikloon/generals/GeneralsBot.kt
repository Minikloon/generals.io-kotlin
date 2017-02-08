package net.minikloon.generals

import io.socket.client.IO
import io.socket.client.Socket
import net.minikloon.generals.api.GameAi
import net.minikloon.generals.game.*
import net.minikloon.generals.game.generalsapi.ApiMethod
import net.minikloon.generals.game.generalsapi.ApiMethod.*
import net.minikloon.generals.game.generalsapi.ApiNotification
import net.minikloon.generals.game.generalsapi.ApiNotification.*
import net.minikloon.generals.game.generalsapi.messages.GameStart
import net.minikloon.generals.game.generalsapi.messages.GameUpdate
import net.minikloon.generals.game.queue.JoinQueue
import net.minikloon.generals.game.queue.JoinQueuePrivate
import org.json.JSONObject
import java.util.concurrent.CompletableFuture

class GeneralsBot private constructor(
        val userId: String,
        val username: String,
        private val socket: Socket,
        private val queuePicker: () -> JoinQueue,
        private val gameAiSupplier: (game: Game, self: Player, bot: GeneralsBot) -> GameAi
) {
    private var gameAi: GameAi? = null
    
    var stars: Int = 0
        private set
    var rank: Int = 0
        private set
    
    private fun afterConnect() {
        send(SET_USERNAME, userId, username)
        
        jsonHandler(GAME_START) {
            val gs = GameStart.parse(it)
            
            val game = Game(gs.replayId, gs.gameChatRoom, gs.teams)
            val gameAi = gameAiSupplier(game, gs.self, this)
            gameAi.onGameStart()
            this.gameAi = gameAi
        }
        
        jsonHandler(GAME_UPDATE) {
            val gameAi = gameAi ?: return@jsonHandler
            val gu = GameUpdate.parse(it)
            
            gameAi.game.update(gu)
            
            val order = gameAi.computeTurn()
            order.execute(this)
        }
        
        handler(GAME_WON) {
            gameAi?.onGameEnd(Won())
            
            send(LEAVE_GAME)
            joinQueue()
        }
        
        jsonHandler(GAME_LOST) {
            val gameAi = gameAi ?: return@jsonHandler
            val killerId = it["killer"] as Int
            val killer = gameAi.game.players[killerId]
            gameAi.onGameEnd(Killed(killer))

            send(LEAVE_GAME)
            joinQueue()
        }
        
        socket.on(Socket.EVENT_DISCONNECT) {
            gameAi?.onGameEnd(Disconnected())
        }
        
        handler(RECEIVE_CHAT) {
            val gameAi = gameAi ?: return@handler
            val chatRoom = it[0] as String
            val json = it[1] as JSONObject
            val text = json["text"] as String
            val senderId = json.opt("playerIndex")
            val sender = if(senderId == null) null else gameAi.game.players[senderId as Int]
            gameAi.onReceiveChat(sender, text)
        }
        
        handler(UPDATE_STARS) {
            println("stars")
        }
        
        jsonHandler(UPDATE_RANK) {
            println("update rank")
        }
        
        joinQueue()
    }
    
    private fun joinQueue() {
        val joinQueue = queuePicker()
        joinQueue.execute(this)
        send(SET_FORCE_START, joinQueue.type.id ?: (joinQueue as JoinQueuePrivate).gameId, true)
        send(REQUEST_STARS_AND_RANK, userId)
    }
    
    fun send(method: ApiMethod, vararg objects: Any) {
        socket.emit(method.str, *objects)
    }
    
    private fun jsonHandler(notification: ApiNotification, fn: (JSONObject) -> Unit) {
        handler(notification) {
            fn(it[0] as JSONObject)
        }
    }
    
    private fun handler(notification: ApiNotification, fn: (Array<Any>) -> Unit) {
        socket.on(notification.str, {
            try {
                fn(it)
            } catch(e: Throwable) {
                System.err.println("Error executing handler for $notification")
                e.printStackTrace()
            }
        })
    }
    
    companion object {
        val botServer = "http://bot.generals.io"
        val botWsServer = "http://botws.generals.io"
        
        fun connect(user: String, username: String, queueJoin: JoinQueue, gameAi: (game: Game, self: Player, bot: GeneralsBot) -> GameAi) : CompletableFuture<GeneralsBot> {
            return connect(user, username, { queueJoin }, gameAi)
        }
        
        fun connect(userId: String, username: String, queuePicker: () -> JoinQueue, gameAi: (game: Game, self: Player, bot: GeneralsBot) -> GameAi) : CompletableFuture<GeneralsBot> {
            val socket = IO.socket(botWsServer)
            
            val future = CompletableFuture<GeneralsBot>()
            
            socket.on(Socket.EVENT_CONNECT) {                
                val bot = GeneralsBot(userId, username, socket, queuePicker, gameAi)
                
                try {
                    bot.afterConnect()
                    future.complete(bot)
                } catch(e: Throwable) {
                    future.completeExceptionally(e)
                }
            }
            
            socket.on(Socket.EVENT_CONNECT_ERROR) {
                future.completeExceptionally(Exception("Connection error: ${it.joinToString()}"))
            }
            
            socket.on(Socket.EVENT_CONNECT_TIMEOUT) {
                future.completeExceptionally(Exception("Connection attempt timeout"))
            }
            
            socket.connect()
            
            return future
        }
    }
}
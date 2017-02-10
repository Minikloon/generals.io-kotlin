# ![asd](http://i.imgur.com/7ELyugM.png) generals.io-kotlin
[![](https://jitpack.io/v/Minikloon/generals.io-kotlin.svg)](https://jitpack.io/#Minikloon/generals.io-kotlin)
[![](https://img.shields.io/badge/Kotlin-1.1.0--beta--38-blue.svg)](https://kotlinlang.org/)

generals.io-kotlin is a Kotlin bot framework for http://generals.io/. It's an opinionated model of Generals' API available at http://dev.generals.io/api. The library's main goal is ease of use. It only supports Generals and doesn't include AI tools such as pathfinding. The implementation uses the official [socket.io Java client library](https://github.com/socketio/socket.io-client-java). The API should work with any JVM language including Java. The library is available under [MIT License](https://tldrlegal.com/license/mit-license).

# Install

generals.io-kotlin is available on Maven through JitPack.

Maven:
~~~~
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
<dependency>
    <groupId>com.github.Minikloon</groupId>
    <artifactId>generals.io-kotlin</artifactId>
    <version>0.1</version>
</dependency>
~~~~

Gradle:
~~~~
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
dependencies {
    compile 'com.github.Minikloon:generals.io-kotlin:0.1'
}
~~~~

# Usage
Create a class extending GameAi:
~~~~
class RandBot(game: Game, self: Player, bot: GeneralsBot) : GameAi(game, self, bot) {
    override fun computeTurn(): Order {
        val attackable = { tile: Tile -> !tile.isCity }
        
        val from = game.world.tiles
                .filter { it.owner == self && it.getRelatives(attackable).isNotEmpty() }
                .firstOrNull() ?: return NoMove()
        
        val relatives = from.getRelatives(attackable)
        val to = relatives[Random().nextInt(relatives.size)]
        
        return Move(from, to)
    }
}
~~~~
This AI has the same behavior as the one at [http://dev.generals.io/api#tutorial](http://dev.generals.io/api#tutorial). All of the information available through the API can be accessed through GameAi's Game object.

Then initialize your bot:
~~~~
val userId = "SyfAzpGuy"
val username = "Kloon Bot"
val joinQueue = JoinQueuePrivate("kloontest")
GeneralsBot.connect(userId, username, joinQueue, ::MyAi).thenRun {
    println("Connected!")
    println(joinQueue.link)
}
~~~~
The third parameter of GeneralsBot::connect can take either a JoinQueue instance of a lambda to a JoinQueue instance. The latter can be used to implement logic on how to pick the queue your bot will play in.

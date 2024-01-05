package example.racecondition

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class AccountCoroutine() {

    private var balance: Int = 0

    constructor(balance: Int) : this() {
        this.balance = balance
    }

    suspend fun getBalance(): Int {
        delay((Math.random() * 1000).toLong())
        return this.balance
    }

    suspend fun updateBalance(value: Int) {
        delay((Math.random() * 1000).toLong())
        this.balance = value
    }

    suspend fun withdraw(value: Int) {
        var bala = this.getBalance()

        if (bala - value > 0) {
            bala -= value

            updateBalance(bala)
            println("Saldo $balance")
        } else {
            println("Saldo insuficiente $balance")
        }
    }

}

fun main(args: Array<String>) = runBlocking<Unit> {
    val account = AccountCoroutine(2000)

    async { account.withdraw(1500) }
    async { account.withdraw(1500) }
    async { account.withdraw(1500) }
    async { account.withdraw(1500) }
}

//Solução via código

/*class AccountCoroutineWithLock() {

    private var balance: Int = 0
    private val lock = ReentrantLock(true)
    private val mutex = Mutex()

    constructor(balance: Int) : this() {
        this.balance = balance
    }

    suspend fun getBalance(): Int {
        delay((Math.random() * 1000).toLong())
        return this.balance
    }

    suspend fun updateBalance(value: Int) {
        delay((Math.random() * 1000).toLong())
        this.balance = value
    }

    suspend fun withdraw(value: Int) {
        mutex.withLock {
            var bala = this.getBalance()

            if (bala - value > 0) {
                bala -= value

                updateBalance(bala)
                println("Saldo $balance")
            } else {
                println("Saldo insuficiente $balance")
            }
        }
    }

}

fun main(args: Array<String>) = runBlocking<Unit> {
    val account = AccountCoroutineWithLock(2000)

    async { account.withdraw(1500) }
    async { account.withdraw(1500) }
    async { account.withdraw(1500) }
    async { account.withdraw(1500) }
}*/

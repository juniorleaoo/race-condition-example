package example.racecondition

import jakarta.persistence.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity


@SpringBootApplication
class RaceConditionExampleApplication

@Entity
@Table(name = "account")
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,

    @Column(name = "balance", nullable = false)
    var balance: Int,
)

@Repository
interface AccountRepository : CrudRepository<Account, Long> {

    @Modifying
    @Query("UPDATE account SET balance = ? WHERE id = 1", nativeQuery = true)
    fun updateBalance(@Param("balance") balance: Int)

    @Query("SELECT balance FROM account WHERE id = 1", nativeQuery = true)
    fun getBalance(): Int
}

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountRepository: AccountRepository
) {

    private val logger = LoggerFactory.getLogger(AccountController::class.java)

    init {
        accountRepository.save(
            Account(
                1,
                2000
            )
        )
    }

    @Transactional
    @GetMapping("/{value}")
    fun withdraw(@PathVariable("value") value: Int): ResponseEntity<Int> {
        var balance = this.accountRepository.getBalance()

        if (balance - value > 0) {
            balance -= value

            this.accountRepository.updateBalance(balance)
            logger.info("withdraw Saldo $balance")
            return ResponseEntity.ok(balance)
        }
        logger.info("withdraw Saldo insuficiente $balance")
        return ResponseEntity.ok(balance)
    }

    @Transactional(readOnly = true)
    @GetMapping()
    fun getBalance(): ResponseEntity<Int> {
        val balance = this.accountRepository.getBalance()
        logger.info("getBalance Saldo $balance")
        return ResponseEntity.ok(balance)
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val logger = LoggerFactory.getLogger(AccountController::class.java)

    runApplication<RaceConditionExampleApplication>(*args)

    val restTemplate = RestTemplate()

    async {
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts/1500")
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts")
    }

    async {
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts/1500")
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts")
    }

    async {
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts/1500")
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts")
    }

    async {
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts/1500")
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts")
    }

    async {
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts/1500")
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts")
    }

    async {
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts/1500")
        restTemplate.getForEntity<Int>("http://localhost:8080/v1/accounts")
    }

    logger.info("finalizou")
}

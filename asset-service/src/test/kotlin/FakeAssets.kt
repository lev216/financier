import ru.otus.otuskotlin.financier.asset.model.Cash
import ru.otus.otuskotlin.financier.asset.model.Deposit
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import java.time.LocalDateTime

fun fakeCash(
    id: String = "assetId",
    sum: BigDecimal = ONE,
    currency: String = "USD",
    userId: String = "userId",
) = Cash(
    id,
    sum,
    currency,
    userId,
)

fun fakeDeposit(
    id: String = "assetId",
    sum: BigDecimal = ONE,
    currency: String = "USD",
    userId: String = "userId",
    startDate: LocalDateTime = LocalDateTime.now(),
    endDate: LocalDateTime = LocalDateTime.now(),
    interestRate: BigDecimal = TEN,
) = Deposit(
    id,
    sum,
    currency,
    userId,
    startDate,
    endDate,
    interestRate,
)
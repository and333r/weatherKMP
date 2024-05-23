package model.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.db.WeatherAppDatabaseKMP

actual class DatabaseDriverFactory actual constructor() {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(WeatherAppDatabaseKMP.Schema, "WeatherAppDatabaseKCMP")
    }
}
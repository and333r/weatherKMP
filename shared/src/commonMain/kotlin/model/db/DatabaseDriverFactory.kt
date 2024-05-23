package model.db

import app.cash.sqldelight.db.SqlDriver
import com.db.WeatherAppDatabaseKMP

expect class DatabaseDriverFactory() {
    fun create(): SqlDriver
}

fun createDatabase(driverFactory: DatabaseDriverFactory): WeatherAppDatabaseKMP {
    return WeatherAppDatabaseKMP(driverFactory.create())
}
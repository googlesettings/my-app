package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Query("UPDATE users SET isLoggedIn = :isLoggedIn WHERE id = :id")
    suspend fun setLoggedIn(id: String, isLoggedIn: Boolean)

    @Query("UPDATE users SET planTier = :planTier WHERE id = :id")
    suspend fun updatePlan(id: String, planTier: String)

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: ProjectEntity): Long

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface SavedContentDao {
    @Query("SELECT * FROM saved_content ORDER BY createdAt DESC")
    fun getAllSavedContent(): Flow<List<SavedContentEntity>>

    @Query("SELECT * FROM saved_content WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavorites(): Flow<List<SavedContentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(content: SavedContentEntity): Long

    @Update
    suspend fun update(content: SavedContentEntity)

    @Query("UPDATE saved_content SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM saved_content WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM saved_content")
    suspend fun clearAll()
}

@Dao
interface BrandVoiceDao {
    @Query("SELECT * FROM brand_voice WHERE id = 1 LIMIT 1")
    fun getBrandVoice(): Flow<BrandVoiceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBrandVoice(voice: BrandVoiceEntity)
}

@Dao
interface UsageDao {
    @Query("SELECT * FROM usage_tracking WHERE id = 1 LIMIT 1")
    fun getUsage(): Flow<UsageEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsage(usage: UsageEntity)
}

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscriptions WHERE id = 1 LIMIT 1")
    fun getSubscription(): Flow<SubscriptionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSubscription(subscription: SubscriptionEntity)
}

@Database(
    entities = [
        UserEntity::class,
        ProjectEntity::class,
        SavedContentEntity::class,
        BrandVoiceEntity::class,
        UsageEntity::class,
        SubscriptionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SellAiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun savedContentDao(): SavedContentDao
    abstract fun brandVoiceDao(): BrandVoiceDao
    abstract fun usageDao(): UsageDao
    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        @Volatile
        private var INSTANCE: SellAiDatabase? = null

        fun getInstance(context: Context): SellAiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SellAiDatabase::class.java,
                    "sellai_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

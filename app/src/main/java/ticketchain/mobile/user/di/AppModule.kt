package ticketchain.mobile.user.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.storage.UserDataService
import ticketchain.mobile.user.storage.userDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun getAccountService(userDataService: UserDataService, state: AppState): AccountService =
        AccountService(userDataService, state)

    @Singleton
    @Provides
    fun getAppState(): AppState = AppState()

    @Singleton
    @Provides
    fun getUserDataService(@ApplicationContext context: Context): UserDataService =
        UserDataService(context.userDataStore)
}
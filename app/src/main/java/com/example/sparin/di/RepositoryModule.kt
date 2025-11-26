package com.example.sparin.di

import com.example.sparin.data.repository.AuthRepository
import com.example.sparin.data.repository.UserRepository
import com.example.sparin.data.repository.RoomRepository
import com.example.sparin.data.repository.CommunityRepository
import com.example.sparin.data.repository.CampaignRepository
import com.example.sparin.data.repository.ChatRepository
import com.example.sparin.data.repository.MatchRepository
import org.koin.dsl.module

/**
 * Repository Module untuk semua repositories
 */
val repositoryModule = module {
    
    // Auth Repository
    single { AuthRepository(get(), get()) }
    
    // User Repository
    single { UserRepository(get(), get()) }
    
    // Room Repository
    single { RoomRepository(get(), get()) }
    
    // Community Repository
    single { CommunityRepository(get()) }
    
    // Campaign Repository
    single { CampaignRepository(get()) }
    
    // Chat Repository
    single { ChatRepository(get(), get()) }
    
    // Match Repository
    single { MatchRepository(get()) }
}

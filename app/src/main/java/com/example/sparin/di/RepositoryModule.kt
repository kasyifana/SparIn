package com.example.sparin.di

import com.example.sparin.data.repository.AuthRepository
import com.example.sparin.data.repository.UserRepository
import com.example.sparin.data.repository.RoomRepository
import com.example.sparin.data.repository.CommunityRepository
import com.example.sparin.data.repository.CampaignRepository
import com.example.sparin.data.repository.ChatRepository
import com.example.sparin.data.repository.MatchRepository
import com.example.sparin.data.repository.FeedRepository
import com.example.sparin.presentation.auth.SignInViewModel
import com.example.sparin.presentation.home.HomeViewModel
import com.example.sparin.presentation.personalization.PersonalizationViewModel
import com.example.sparin.presentation.discover.DiscoverViewModel
import com.example.sparin.presentation.community.CommunityViewModel
import com.example.sparin.presentation.community.feed.CommunityFeedViewModel
import com.example.sparin.presentation.profile.ProfileViewModel
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
    single { CommunityRepository(get(), get()) }
    
    // Campaign Repository
    single { CampaignRepository(get()) }
    
    // Chat Repository
    single { ChatRepository(get(), get()) }
    
    // Match Repository
    single { MatchRepository(get()) }

    // Feed Repository
    single { FeedRepository(get(), get()) }
    
    // ViewModels
    factory { SignInViewModel(get()) }
    factory { HomeViewModel(get(), get(), get(), get()) } // UserRepo, RoomRepo, MatchRepo, CampaignRepo
    factory { PersonalizationViewModel(get(), get()) }
    factory { DiscoverViewModel(get()) } // RoomRepo
    factory { CommunityViewModel(get(), get()) } // CommunityRepo + FirestoreService
    factory { CommunityFeedViewModel(get(), get()) } // FeedRepo + AuthRepo
    factory { ProfileViewModel(get(), get()) } // UserRepo + RoomRepo
}

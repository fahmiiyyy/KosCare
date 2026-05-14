package com.example.koscare.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClientProvider {

    private const val SUPABASE_URL =
        "https://kogduirjtkpobbnelgrm.supabase.co"

    private const val SUPABASE_KEY =
        "sb_publishable_PbhCmhssp1aVEJxyVCc4tw_1LSg4puK"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {

        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
package com.eliascoelho911.homeapp.robotcar.data.service

interface OutputService {
    suspend fun connect()

    suspend fun write(byteArray: ByteArray)

    suspend fun flush()

    fun close()
}
package com.yamilmedina.gallerypublish.config

import kong.unirest.Unirest
import kong.unirest.UnirestInstance

object UnirestClient {

    private val unirest: UnirestInstance by lazy { Unirest.primaryInstance() }

    fun get(url: String) = unirest.get(url)!!
    fun post(url: String) = unirest.post(url)!!
    fun put(url: String) = unirest.put(url)!!
}
package com.github.jleskovar.btcrpc.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.googlecode.jsonrpc4j.IJsonRpcClient
import com.googlecode.jsonrpc4j.JsonRpcClient
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketFactory
import java.lang.reflect.Type
import javax.net.ssl.SSLContext

/**
 * Created by james on 21/12/17.
 */

abstract class AbstractJsonWebSocketRpcClient(wsUrl: String, sslContext: SSLContext)
    : JsonRpcClient(ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)), IJsonRpcClient {

    private val webSocketFactory = WebSocketFactory()

    protected val socket: WebSocket

    init {
        webSocketFactory.sslContext = sslContext
        socket = webSocketFactory.createSocket(wsUrl)
    }

    abstract override fun invoke(methodName: String?, argument: Any?, returnType: Type?, extraHeaders: MutableMap<String, String>?): Any?

    protected abstract fun handleTextMessage(text: String?)

    fun connect() {
        socket.addListener(object: WebSocketAdapter() {
            override fun onTextMessage(websocket: WebSocket?, text: String?) {
                handleTextMessage(text)
            }
        })

        socket.connect()
    }

    fun disconnect() {
        socket.disconnect()
        // Hack to ensure reading close timer task is not running by the time our application quits
        Thread.getAllStackTraces().keys.filter { it.name == "ReadingThreadCloseTimer" }.forEach { it.stop() }
    }

    protected fun fastExtractId(jsonRpc: String) = jsonRpc.substringAfterLast("\"id\":\"").substringBefore("\"")

    protected fun hasError(jsonRpcResponse: String) = !jsonRpcResponse.contains("\"error\":null")

    protected fun extractError(jsonRpcResponse: String): Pair<Int, String> {
        val errorNode = ObjectMapper().readTree(jsonRpcResponse)["error"]
        val errorCode = errorNode["code"].asInt()
        val errorMessage = errorNode["message"].asText()
        return Pair(errorCode, errorMessage)
    }

    override fun invoke(methodName: String?, argument: Any?) {
        invoke(methodName, argument, null as Type?)
    }

    override fun invoke(methodName: String?, argument: Any?, returnType: Type?): Any? {
        return invoke(methodName, argument, returnType, mutableMapOf())
    }

    override fun <T : Any?> invoke(methodName: String?, argument: Any?, clazz: Class<T>?): T {
        return invoke(methodName, argument, clazz, mutableMapOf())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> invoke(methodName: String?, argument: Any?, clazz: Class<T>?, extraHeaders: MutableMap<String, String>?): T {
        return invoke(methodName, argument, clazz as Type, extraHeaders) as T
    }
}

class JsonRpcError(val code: Int, val errorMessage: String) : Exception(errorMessage)
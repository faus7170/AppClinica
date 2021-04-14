package com.example.appclinica.ui.chat

class MensajeEnviar: DatosMensaje {

    lateinit var map: Map<String,String>

    /*constructor(contenido: String, from: String, map: Map<*, *>) : super(contenido, from) {
        this.map = map
    }

    constructor(map: Map<*, *>) {
        this.map = map
    }*/

    constructor(contenido: String, from: String, map: Map<String, String>) : super(contenido, from) {
        this.map = map
    }

    constructor()

}
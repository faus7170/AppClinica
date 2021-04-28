package com.example.appclinica.ui.chat.modelo

class MessageSender: DatosMensaje {

    lateinit var hora: Map<String,String>

    constructor(sender: String, reciver: String, msm: String, hora: Map<String, String>) : super(sender, reciver, msm) {
        this.hora = hora
    }

    constructor()
}
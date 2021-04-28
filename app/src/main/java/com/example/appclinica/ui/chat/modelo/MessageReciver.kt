package com.example.appclinica.ui.chat.modelo

class MessageReciver: DatosMensaje {

    var hora : Long = 0

    constructor(sender: String, reciver: String, msm: String, hora: Long) : super(sender, reciver, msm) {
        this.hora = hora
    }

    constructor()
}
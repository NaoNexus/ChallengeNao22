import socket


class MyClass(GeneratedClass):
    def __init__(self):
        GeneratedClass.__init__(self)

    def onLoad(self):
        pass

    def onUnload(self):
        pass

    def onInput_onStart(self):
        self.initServer()
        pass

    def onInput_onStop(self):
        self.onUnload()  # it is recommended to reuse the clean-up as the box is stopped
        self.onStopped()  # activate the output of the box

    def initServer(self):
        host = ''
        port = 5050

        print('Server started')

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((host, port))
        s.listen(5)

        self.startServer(s)

    def startServer(self, s):
        msg = ['', '']

        while True:
            c, addr = s.accept()
            msg = self.on_new_client(c, msg)
            c.close()

    def on_new_client(self, clientSocket, msg):
        message = clientSocket.recv(1024).decode('utf-8')
        message = message.split('_')

        if 'app' in message:
            if (msg[0] != ''):
                msg[0] = message[0]
                print('Recieve from app: ', message)
            msg = ['', '']

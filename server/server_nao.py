import socket

HOST = socket.gethostbyname(socket.gethostname())
APP = socket.gethostbyname('')  # Add device name in order to get his IP
ARD = socket.gethostbyname('')  # Add device name in order to get his IP


def initServer():
    host = ''
    port = 5050

    print('Server started')
    print(f'Port: {port}\n')

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((host, port))
    s.listen(5)

    print('| DEVICE | IP')
    print(f'|  NAO   | {HOST}')
    print(F'|  APP   | {APP}')
    print(F'|ARDUINO | {ARD}')

    startServer(s)


def startServer(s):
    msg = ['', '']

    while True:
        c, addr = s.accept()
        msg = on_new_client(c, msg)
        c.close()


def on_new_client(clientSocket, msg):
    message = clientSocket.recv(1024).decode('utf-8')
    message = message.split('_')

    if 'app' in message:
        msg = message
        print('Recieve from app: ', msg[0])
    return msg


def send(data, ip, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((ip, port))
    s.send(data.encode())
    s.close()


if __name__ == '__main__':
    initServer()

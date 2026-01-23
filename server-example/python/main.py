import json
import base64
from http.server import BaseHTTPRequestHandler, HTTPServer
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes

# Secret AES key from Notifer
SECRET_KEY_B64 = "WE5t3GiPTDWTtpBdRunW73PBxdJLHlAsEuNwZEVxQY0="
SECRET_KEY = base64.b64decode(SECRET_KEY_B64)


class Handler(BaseHTTPRequestHandler):
    def do_POST(self):
        content = self.rfile.read(int(self.headers["Content-Length"])).decode("utf-8")
        print("Raw:", content)

        json_content = json.loads(content)

        if "iv" in json_content:
            iv = base64.b64decode(json_content["iv"])
            body = base64.b64decode(json_content["body"])
            ciphertext = body[:-16]
            tag = body[-16:]

            decryptor = Cipher(algorithms.AES(SECRET_KEY), modes.GCM(iv, tag)).decryptor()
            plaintext = decryptor.update(ciphertext) + decryptor.finalize()

            print("Body:", plaintext.decode("utf-8"))

        self.send_response(200)
        self.end_headers()


if __name__ == "__main__":
    HTTPServer(("", 4444), Handler).serve_forever()


from http.server import HTTPServer, SimpleHTTPRequestHandler

if __name__ == '__main__':
    httpd = HTTPServer(('0.0.0.0', 8000), SimpleHTTPRequestHandler)
    httpd.serve_forever()
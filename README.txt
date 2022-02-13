SETUP: 
Import Project into eclipse from File-> import

EXECUTION:
Run the Server, then the host, then client. In that order.

DESCRIPTION:
Client sends alternating messages to intermediate host, which forwards to server for validation. The server replies to host and host back to client if the request was valid.

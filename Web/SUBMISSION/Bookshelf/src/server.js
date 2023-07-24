const http = require('http');
const {RouterManager} = require('../utils/router');

const requestListener = (request, response) => {
    response.setHeader('Content-Type', 'text/html');
    const route = new RouterManager(request, response);
    route.proceed();
};

const server = http.createServer(requestListener);
const port = 9000;
const host = 'localhost';
 
server.listen(port, host, () => {
    console.log(`Server berjalan pada http://${host}:${port}`);
});


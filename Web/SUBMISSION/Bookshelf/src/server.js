const http = require('http');
const {MetodeManager} = require('../utils/page');
const {RouterManager} = require('../utils/router');

const requestListener = (request, response) => {
    const mMethode = new MetodeManager(request, response);
    const mRouter = new RouterManager(request, response);
    const method = mMethode.getMethod(mMethode);
    const route = mRouter.getRoute;
    response.setHeader('Content-Type', 'text/html');

    
    route('/').method('GET');
    const { url } = request;

    switch (url) {
        case '/books':
            method.get().home()
            break;
    
        default:
            response.statusCode = 410;
            response.end('<h1>Halaman tidak ditemukan!</h1>');
            break;
    }
    
};
const server = http.createServer(requestListener);

const port = 9000;
const host = 'localhost';
 
server.listen(port, host, () => {
    console.log(`Server berjalan pada http://${host}:${port}`);
});


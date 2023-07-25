import  * as http  from "http";
import {RouterManager} from '../utils/router.js';

const requestListener = (request, response) => {
    response.setHeader('Content-Type', 'application/json');
    response.setHeader('X-Powered-By', 'NodeJS');
    const route = new RouterManager(request, response);
    route.proceed();
};

const server = http.createServer(requestListener);
const port = 9000;
const host = 'localhost';
 
server.listen(port, host, () => {
    console.log(`Server berjalan pada http://${host}:${port}`);
});


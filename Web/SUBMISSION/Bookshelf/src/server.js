import Hapi from '@hapi/hapi';
import { router } from "./router.js";

const init = async () => {
    const server = Hapi.server({
        port: 9000,
        host: 'localhost',
        routes:{
            cors:{
                origin: ['*'],
            }
        }
    });

    server.route(router);

    await server.start();
    console.log(`Server berjalan pada ${server.info.uri}`);
};

init();

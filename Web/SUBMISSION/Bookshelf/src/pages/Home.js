const MHelper = require('../../utils/helper');

const home = function(request, response){
    const { method } = request;
    const mHelper = new MHelper();
    let message = 'Gone !!!';
    const data = ["Roxy", "Anna", "Ricco", "Ronaldo","Mario", "Elisabeth"];

    switch (method) {
        case 'GET':
            if (!data) {
                message = 'Tidak ada data!!!!';
            }else{
                message = null;
                mHelper.message = message;
                mHelper.data = data;
            }
            const responMessage = mHelper.get();
            response.statusCode = responMessage.statusCode;
            response.end(`<h1>${responMessage.getMessage}</h1>`);
            break;
        case 'POST':
            
            response.statusCode = 410;
            response.end(`<h1>${message}</h1>`);
            break;
    
        default:
            response.statusCode = 410;
            response.end('<h1>Halaman tidak ditemukan!</h1>');
            break;
    }
    
}

module.exports = {home};
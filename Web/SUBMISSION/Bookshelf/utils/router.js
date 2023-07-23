const {PageManager} = require('./page')

class RouterManager{
    constructor(request, response){
        this.request = request;
        this.response = response;
    }

    getRoute(route){
        return{
            method: (method) =>{ init(route, method, this.request, this.response) }
        }
    }
}

function init(route, method, request, response) {
    const mPage = new PageManager(request, response);
    const page = mPage.getPage(mPage);
    let bookid = 0;
    
    switch (route) {
        case '/books':
            switch (method) {
                case 'GET':
                    page.get().home();
                    break;
                case 'POST':
                    page.post().home();
                    break;
                default:
                    //405 Method Not Allowed
                    break;
            }
            break;
        case `/books/${bookid}`:
            switch (method) {
                case 'GET':
                    page.get().detail(bookid);
                    break;
                case 'PUT':
                    page.put().detail(bookid);
                    break;
                case 'DELETE':
                    page.delete().remove(bookid);
                    break;
            
                default:
                    break;
            }
            break;
    
        default:
            //400 Bad Request
            mPage.response.statusCode = 400;
            const errorPage = mPage.getPage(mPage);
            errorPage.error();
            break;
    }
    
}

module.exports = {RouterManager};
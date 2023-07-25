import {PageManager} from './page.js';

class RouterManager{
    constructor(request, response){
        this.request = request;
        this.response = response;
    }

    proceed(){
        init(this.request, this.response);
    }
}

function init(request, response) {
    const mPage = new PageManager(request, response);
    const page = mPage.getPage(mPage);
    const { method, url } = request;
    let bookid = url.split('/books/');
    
    switch (url) {
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
            console.log(bookid);
            if (bookid != '') {
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
            } else {
                //400 Bad Request
                mPage.response.statusCode = 400;
                const errorPage = mPage.getPage(mPage);
                errorPage.error();
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

export {RouterManager};
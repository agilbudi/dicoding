import {home} from '../src/pages/Home.js';

class PageManager{
    constructor(request, response){
        this.request = request;
        this.response = response;
    }
//open log
    getPage(){
        return{
            get: () => { 
                return{
                    home: () => { home(this.request, this.response) },
                    detail:(bookid) => {},
                    error: () =>{
                        this.response.end('<h1>Halaman tidak ditemukan!</h1>');
                    }
                }
            },
            post: () =>{
                return{
                    home: () => { home(this.request, this.response) },
                }
            },
            put: () =>{
                return{
                    detail:(bookid) => {},
                }
            },
            delete: () =>{
                return{}
            },
            error:() =>{
                this.response.end('<h1>Halaman tidak ditemukan!</h1>');
            }
        }
    }
}


export {PageManager};
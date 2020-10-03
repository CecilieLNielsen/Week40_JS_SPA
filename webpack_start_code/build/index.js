import "./style.css"
import "bootstrap/dist/css/bootstrap.css"
import "./jokeFacade"
import jokeFacade from "./jokeFacade"


function makeListItems() {
    console.log('here? :O ')
const jokes = jokeFacade.getJokes();
let jokelis = jokes.map(joke => `<li> ${joke} </li>`)
const lisitemsAsStr = jokelis.join("");
document.getElementById("jokes").innerHTML = lisitemsAsStr;
}
console.log('luv ya!')
makeListItems(); 
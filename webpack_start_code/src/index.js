import 'bootstrap/dist/css/bootstrap.css'
import jokes from "./jokes";
// import navigation from "./navigation";


// OPGAVE 1.2
const allJokes = jokes.getJokes().map(joke => "<li>"+joke+"</li>");
document.getElementById("jokes").innerHTML = allJokes.join("");

function fetchJoke() {
    const jokeID = document.getElementById("inputId").value;
    const joke = jokes.getJokeById(jokeID);
    document.getElementById("joke").innerHTML = joke;
}

document.getElementById("fetchJoke").addEventListener("click", function (event) {
    event.preventDefault();
    fetchJoke();
});

// OPGAVE 1.3
function addJoke() {
    const joke = document.getElementById("inputJoke").value;
    document.getElementById("inputJoke").value = "";
    jokes.addJoke(joke);
    const allJokes = jokes.getJokes().map(joke => "<li>"+joke+"</li>");
document.getElementById("jokes").innerHTML = allJokes.join("");
}

document.getElementById("addJoke").addEventListener("click", function (event) {
    event.preventDefault();
    addJoke();
});

// OPGAVE 2
function fetchQuote() {
    const url = "https://studypoints.info/jokes/api/jokes/period/hour";
    fetch(url)
        .then(res => res.json())
        .then(data => document.getElementById("quote").innerHTML = data.joke);    
    }

    document.getElementById("fetchQuote").addEventListener("click", function (event) {
        event.preventDefault();
        fetchQuote();
    });


function my_callback (parameter) {
return parameter > 0;
}

function myFilter (myList, callback) {
    return myList.filter(callback);
}

const myList = [10,20,30,40,50,60,70,80,90,100]

console.log(myFilter(myList, my_callback));

// Opgave 1 - a
const nameList = ["Hassan", "Jan", "Peter", "Boline", "Frederik", "Jon", "Hans"];
const namesWithA = nameList.filter((element) => { return element.includes("a")});

console.log(namesWithA);

// Opgave 1 - b
const namesReversed = nameList.map((element) => { return element.split("").reverse().join("")});

console.log(namesReversed);

// Opgave 2 - a
function myFilter(array, callback) {
    const list = [];
    for (idx in array) {
        if (callback(array[idx])){
            list.push(array[idx]);
        }
    }
    return list;
}

const namesWithA2 = myFilter(nameList, (element) => { return element.includes("a")});

console.log(namesWithA2);

// Opgave 2 - b
function myMap(array, callback) {
    const list = [];
    for (idx in array) {
        list.push(callback(array[idx])); 
    }
    return list;

}

const namesReversed2 = myMap(nameList, (element) => { return element.split("").reverse().join("")})

console.log(namesReversed2);

// Opgave 3 - a
var numbers = [1, 3, 5, 10, 11];

let newNumbers = numbers.map(function callback(currentValue, index, array) {
    if (typeof array[index + 1] !== 'number') {
        return currentValue;
    }
    return currentValue + array[index + 1];
})

console.log(newNumbers);

// Opgave 3 - b
const mappedNames = nameList.map(name => '<a href="">' + name + '</a>');
console.log(mappedNames);
const result = '<nav>' + mappedNames.join('') + '</nav>';
console.log(result);

// Opgave 3 - c
var persons = [{name:"Hassan",phone:"1234567"}, {name: "Peter",phone: "675843"}, {name: "Jan", phone: "98547"},{name: "Boline", phone: "79345"}];

var table = "<table><tr><th>Name</th><th>Phone</th></tr>"

table += persons.map(person => '<tr><td>' + person.name + '</td><td>' + person.phone + '</td></tr>').join('');

table += "</table>";

console.log(table);

// Opgave 4 - a
var all= ["Hassan", "Peter", "Carla", "Boline"];
const joinedNames = all.join('#');

console.log(joinedNames);

// Opgave 4 - b
const numbers2 = [2, 3, 67, 33];
const reduced = numbers2.reduce((accumulator, currentValue) => accumulator + currentValue);
//const reduced = numbers2.reduce((accumulator, currentValue) => { return accumulator + currentValue});
console.log(reduced);

// Opgave 4 - c
const members = [{name : "Peter", age: 18},{name : "Jan", age: 35},{name : "Janne", age: 25},{name : "Martin", age: 22}];

var reducer = function(accumulator, member, index, arr){
    if(arr.length - 1 === index){
        return (accumulator + member.age) / arr.length;
    }
    return accumulator + member.age; 
}

const avgAge = members.reduce(reducer, 0);
console.log(avgAge);
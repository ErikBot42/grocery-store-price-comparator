window.addEventListener('load', ()=>{
    console.log('Page Loaded')
    let Table = document.querySelector('table')
    let allDeleteButtons = document.querySelectorAll('.btn-danger');
    let createButton = document.querySelector('#createButton')
    let name = document.querySelector('#name')
    let price = document.querySelector('#price')
    let store = document.querySelector('#store')
    for (let index = 0; index < allDeleteButtons.length; index++) {
        allDeleteButtons[index].addEventListener('click', (e)=>{
            console.log('Delete product');
            allDeleteButtons[index].closest('tr').remove();
        })
        
    }
    createButton.addEventListener('click', ()=>{

        console.log('Creating new Product');
        event.preventDefault();
        console.log('Name ' + name.value + ' Price ' + price.value + ' Store' + store.value);
        createNewRow(name.value,price.value,store.value);
        name.value = ''
        price.value = ''
        store.value = ''
        
    })

    function createNewRow(name, price, store){
        
    }
})

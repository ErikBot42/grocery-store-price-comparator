window.addEventListener('load', ()=>{
    console.log('Page Loaded')
    let Table = document.querySelector('table')
    let allDeleteButtons = document.querySelectorAll('.btn-danger')
    let createButton = document.querySelector('#createButton')
    let name = document.querySelector('#name')
    let price = document.querySelector('#price')
    let store = document.querySelector('#store')
    for (let index = 0; index < allDeleteButtons.length; index++) {
        allDeleteButtons[index].addEventListener('click', (e)=>{
            console.log('Delete product')
            allDeleteButtons[index].closest('tr').remove()
        })
        
    }
    createButton.addEventListener('click', ()=>{

        console.log('Creating new Product')
        event.preventDefault()
        console.log('Name ' + name.value + ' Price ' + price.value + ' Store' + store.value)
        createNewRow(name.value,price.value,store.value)
        name.value = ''
        price.value = ''
        store.value = ''
        
    })

    function createNewRow(name, price, store){
        var row = Table.insertRow()
        var cell = row.insertCell()
        cell.innerHTML = name
        var cell = row.insertCell()
        cell.innerHTML = price
        var cell = row.insertCell()
        cell.innerHTML = store
        var cell = row.insertCell()

        const newUl = document.createElement('ul')
        newUl.setAttribute('class', '')
        cell.appendChild(newUl)

        const newEditLi = document.createElement('li')
        newUl.appendChild(newEditLi)
        newEditLi.setAttribute('class', 'list-inline-item')
        const newEditButton = document.createElement('button')
        newEditLi.appendChild(newEditButton)
        newEditButton.setAttribute('class', 'btn btn-success btn-sm rounded-0')
        newEditButton.setAttribute('type', 'button')
        newEditButton.setAttribute('data-toggle','tooltip')
        newEditButton.setAttribute('data-placement', 'top')
        newEditButton.setAttribute('title', 'edit')
        newEditButton.textContent = 'Edit'

        const newDeleteLi = document.createElement('li')
        newUl.appendChild(newDeleteLi);
        newDeleteLi.setAttribute('class', 'list-inline-item')
        const newDeleteButton = document.createElement('button')
        newDeleteLi.appendChild(newDeleteButton)
        newDeleteButton.setAttribute('class', 'btn btn-danger btn-sm rounded-0')
        newDeleteButton.setAttribute('type', 'button')
        newDeleteButton.setAttribute('data-toggle','tooltip')
        newDeleteButton.setAttribute('data-placement', 'top')
        newDeleteButton.setAttribute('title', 'edit')
        newDeleteButton.textContent = 'Delete'
        newDeleteButton.addEventListener('click',()=>{
            newDeleteButton.closest('tr').remove()
        })
    }
})

const express = require('express')
const app = express()
const port = 3000

app.get('/getUser', (req, res) => res.json({userName : 'Yassine'}))
app.listen(port, () => console.log(`Example app listening on port ${port}!`))
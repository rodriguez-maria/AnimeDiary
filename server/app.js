const path = require('path')
const bodyParser = require('body-parser')
const log = require('npmlog')
const config = require('./utils/config')
const express = require('express')
const swaggerUi = require('swagger-ui-express')
const swaggerDocument = require('./docs/swagger')
require('express-async-errors') // To handle uncaught async errors.
require('./utils/db') // Connect to database.

const authMiddleware = require('./middlewares/auth')

// Import controllers.
const defaultController = require('./controllers/default_controller')
const sessionController = require('./controllers/session_controller')
const animeController = require('./controllers/anime_controller')

// Create and setup express app.
const app = express()
app.use(bodyParser.urlencoded({
    extended: false
})) // Allow parsing application/x-www-form-urlencoded.
app.use(bodyParser.json()) // Allow parsing application/json.
app.use(express.static(path.join(__dirname, 'public'))) // Make contents of public directory directly accessible.

// Start the app.
app.listen(config.port, () => log.info('app', 'App listening on port %j.', config.port))

// Unauthenticated APIs.
app.get('/', defaultController.home)
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument))
app.post('/api/login', sessionController.login)
app.post('/api/register', sessionController.register)

// Authenticated APIs.
app.use(authMiddleware.authorize)
app.get('/api/animes', animeController.getAnimes)
app.put('/api/animes', animeController.updateAnime)
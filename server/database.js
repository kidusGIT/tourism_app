import { Sequelize, } from 'sequelize';

const sequelize = new Sequelize('toursim', 'root', '', {
    dialect: 'sqlite',
    storage: 'database.sqlite',
});

export default sequelize;
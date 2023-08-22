import { DataTypes } from 'sequelize'

// import user module here
import sequelize from '../database.js';

const Toursim = sequelize.define('Toursim', {
    name:{
        type: DataTypes.STRING,
        allowNull:false,
    },
    desc: {
        type: DataTypes.TEXT,
        defaultValue: '',
    },
    cover_image: {
        type: DataTypes.STRING(3000),
        defaultValue:'',
    },
});

const toursim = async () => {
    await Toursim.sync();
}

toursim();
export default Toursim;
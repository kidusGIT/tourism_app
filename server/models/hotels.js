import { DataTypes } from 'sequelize';

// imprt user built module here
import sequelize from '../database.js';
import Toursim from './toursims.js';

const Hotel = sequelize.define('Hotel', {
      name:{
        type:DataTypes.STRING,
        allowNull:false,
      },
      desc:{
        type:DataTypes.TEXT,
        defaultValue:'',
      },
      img:{
        type:DataTypes.STRING(3000),
        defaultValue:'',
      },
      latitude:{
        type:DataTypes.FLOAT,
        defaultValue:0.0,
      },
      longtude:{
        type:DataTypes.FLOAT,
        defaultValue:0.0,
      },
      
})

Hotel.belongsTo(Toursim);

const hotel = async () => {
  await Hotel.sync();
}

hotel();
export default Hotel;



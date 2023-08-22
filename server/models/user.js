import { DataTypes } from 'sequelize';

// imprt user built module here
import sequelize from '../database.js';

const User = sequelize.define('User', {
      full_name:{
        type:DataTypes.STRING,
        allowNull:false,
      },
      username:{
        type:DataTypes.TEXT,
        unique:true,
      },
      password:{
        type:DataTypes.STRING(1500),
        allowNull:false,
      }
      
})

const user = async () => {
  await User.sync();
}

user();
export default User;



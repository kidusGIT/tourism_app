import { DataTypes } from "sequelize";

// import user module here
import sequelize from "../database.js";
import Toursim from "./toursims.js";

const Image = sequelize.define('Image', {
    img_url:{
        type:DataTypes.STRING(3000),
        defaultValue:'img',
    }
});

Image.belongsTo(Toursim);

const image = async () => {
    await Image.sync();
}

image();
export default Image
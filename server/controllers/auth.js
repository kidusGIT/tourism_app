import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';

import User from '../models/user.js';

const getUser =  async (user) => {
    const username = await User.findOne({
        where:{
           username:user          
        },      
    })
    
    return username
}

// create user
export const createUser = async (req, res) => {
 
    try {
        const users = await getUser(req.body.username);
        if(users) return res.status(200).json("Username is used")
        
        const salt = await bcrypt.genSalt(10);
        const hashpwd = await bcrypt.hash(req.body.password, salt);
        
        const user = new User({
            full_name:req.body.full_name,
            username:req.body.username,
            password:hashpwd
        })

        await user.save(); 
        return res.status(200).json("user");
    } catch (err) {
        console.log(err)
        return res.status(400).send('username is in use');
    }
}

// login user
export const signin = async (req, res) => {
   console.log("username: ", req.body.username)
    try {
        const user = await User.findOne({
            where:{
                username:req.body.username          
             }, 
         })
        if(!user) return res.status(200).json('Username not found')

        const validPwd = await bcrypt.compare(req.body.password, user.password)
        if(!validPwd) return res.status(200).json("Invalid password")
       
        const token = jwt.sign({
            id:user._id,
            name:user.name
        }, process.env.JWT)
        
       return res.status(200).json(token)

    } catch (error) {
        console.log(error)
        return res.status(200).json('erro')
    }
}
import jwt from "jsonwebtoken";

export const verfiyToken = (req, res, next) => {
    const token = req.headers.token;

    if(!token) return res.status(400).json("Not Authenticated")

    try {
        const validToken = jwt.verify(token, process.env.JWT)
        req.user = validToken
        next()
    } catch (err) {
        res.status(400).json("Inavlid Token")
    }
}
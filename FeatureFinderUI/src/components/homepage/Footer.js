import React, {Component} from "react";
import Typography from '@material-ui/core/Typography';
import ButtonBase from '@material-ui/core/ButtonBase';
import FacebookImage from "./images/facebook.jpg";
import "./footer.css";

class Footer extends Component {
    constructor(props) {
        super(props);
    }
    render() {
      return (
        <div className="footer">
           <img src={FacebookImage} className="footer_facebook" width="6%" alt="facebook"></img> 
           <Typography variant="body2" color="white" align="center">
                    Director of Music: Charmaine Worrall  ~  Registered Charity No. 1086232  ~  Est. 1949
            </Typography>
        </div>  
      );
    }
  }
export default Footer;
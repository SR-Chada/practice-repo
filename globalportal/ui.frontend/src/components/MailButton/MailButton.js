import React, {Component} from 'react';
import {MapTo} from '@adobe/aem-react-editable-components';
import {Link} from "react-router-dom";
import '../Button/Button.scss';

export const MailButtonEditConfig = {

    emptyLabel: 'Mail Button',

    isEmpty: function(props) {
        return !props || !props.text || props.text.trim().length < 1;
    }
};

export class MailButton extends Component {  
  get mailButtonContent() {    

    return (
      <a
      href={"mailto: "+this.props.emailAddress}
      className="cmp-button cmp-button__action-link"
      id={this.props.id}
      target={this.props.buttonLink?.attributes?.target}
    >
      <span className="cmp-button__text"> {this.props.text}</span>
    </a>
    );
    }
  
  render() {
    if (MailButtonEditConfig.isEmpty(this.props)) {
      return null;
    }

    return <div className="button">{this.mailButtonContent}</div>;
  }
}


MapTo('globalportal/components/mailbutton')(MailButton, MailButtonEditConfig);
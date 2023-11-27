import React, {Component} from 'react';
import {MapTo} from '@adobe/aem-react-editable-components';
require('./Embed.scss')
export const EmbedEditConfig = {
    emptyLabel: 'Embed',
    isEmpty: function (props) {
        console.log(props);
        return !props || !props.list || props.list.trim().length < 1;
    }
};

export  class Embed extends Component {
      get content() {
          return (
<div>
            <iframe 
            allowFullScreen="true" 
            frameBorder="0" 
            id={this.props.id} 
            mozallowfullscreen="true" 
            scrolling="no" 
            src={this.props.youtube}
            webkitallowfullscreen="true">nbsp;
                  </iframe>
</div>
         );
    }


    render() {   
        return (
<div className="cmp-video">
                {this.content}
</div>
        );
    }
}


MapTo('globalportal/components/embed')(Embed, EmbedEditConfig );
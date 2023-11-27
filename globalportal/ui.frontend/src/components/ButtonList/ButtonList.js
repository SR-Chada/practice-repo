import React, {Component} from 'react';
import {MapTo} from '@adobe/aem-react-editable-components';
import {Button} from '../Button/Button';
import Title from '../Title/Title';

require('./ButtonList.scss');

export const ButtonListEditConfig = {

    emptyLabel: 'Button List',

    isEmpty: function (props) {
        return props.listItems == null || props.listItems.length === 0;
    }
};


export class ButtonList extends Component {

    get buttonListContent() {
        return (
            <> {this.props.listItems.map((item, index) =>
                <li className='cmp-buttonlist__item' key={index} onClick={() => {
                   document.title = item.title
                 }}>
                    <Button text = {item.title}
                     buttonLink = {item} />
                </li>)} </>
            );
        }
        get titleContent() {
            return (
                <div className="cm-buttonlist__title cmp-title--align-center">
                    <Title text = {this.props.title} />
                </div>)
        }

    render() {
        if (ButtonListEditConfig.isEmpty(this.props)) {
            return null;
        }

       return (
           <div className="cmp-buttonlist">
             {this.titleContent} 
              <ul className="cmp-buttonlist__items">
                 {this.buttonListContent} 
              </ul>
                 
            </div>
        )
    }
}

MapTo('globalportal/components/buttonlist')(ButtonList, ButtonListEditConfig);

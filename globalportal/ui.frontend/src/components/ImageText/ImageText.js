import React, {Component} from 'react';

import {
    MapTo,
    withComponentMappingContext,
    AllowedComponentsContainer
} from '@adobe/aem-react-editable-components';
import  Container  from '../Container/Container';
require('./ImageText.scss');



const ImageTextEditConfig = {
    emptyLabel: 'Container',

    isEmpty: function(props) {
        return !props || !props.cqItemsOrder || props.cqItemsOrder.length === 0;
    }
};

export default class ImageText extends Component {
    render() {
        if (ImageTextEditConfig.isEmpty(this.props)) {
            return null;
          }
      
          return <div className="cmp-imagetext"> <Container /> </div>;
    }
}

MapTo('globalportal/components/imagetext')(withComponentMappingContext(AllowedComponentsContainer), ImageTextEditConfig);
import React, {Component} from 'react';

import {
    MapTo,
    withComponentMappingContext,
    AllowedComponentsContainer
} from '@adobe/aem-react-editable-components';

import  Container  from '../Container/Container';
require('./ImagePack.scss')



const ContainerConfig = {
    emptyLabel: 'Container',

    isEmpty: function(props) {
        return !props || !props.cqItemsOrder || props.cqItemsOrder.length === 0;
    }
};
export default class ImagePack extends Component {
    render() {
        if (ContainerConfig.isEmpty(this.props)) {
            return null;
          }
      
          return <div className="image-pack"> <Container /> </div>;
    }
}

MapTo('globalportal/components/imagepack')(withComponentMappingContext(AllowedComponentsContainer), ImagePack);
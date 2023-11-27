import React, {Component} from 'react';

import {
    MapTo,
    withComponentMappingContext,
    AllowedComponentsContainer
} from '@adobe/aem-react-editable-components';
import  Container  from '../Container/Container';

require("./RegionSelectionContainer.scss");

const RegionSelectionContainerEditConfig = {
    emptyLabel: 'Container',

    isEmpty: function(props) {
        return !props || !props.cqItemsOrder || props.cqItemsOrder.length === 0;
    }
};

export default class RegionSelectionContainer extends Component {
    render() {
        if (RegionSelectionContainerEditConfig.isEmpty(this.props)) {
            return null;
          }
      
          return <Container />;
    }
}

MapTo('globalportal/components/regionselectioncontainer')(withComponentMappingContext(AllowedComponentsContainer), RegionSelectionContainerEditConfig);
/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// import React from 'react';
import React, { useContext } from 'react';
import 'react-tagsinput/react-tagsinput.css';
import { FormattedMessage } from 'react-intl';
import Typography from '@material-ui/core/Typography';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import Checkbox from '@material-ui/core/Checkbox';
import { APIContext } from 'AppComponents/Apis/Details/components/ApiContext';
import { isRestricted } from 'AppData/AuthManager';

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    mainTitle: {
        paddingTop: theme.spacing(3),
    },
    gatewayPaper: {
        marginTop: theme.spacing(2),
    },
    content: {
        marginTop: theme.spacing(2),
        margin: `${theme.spacing(2)}px 0 ${theme.spacing(2)}px 0`,
    },
    emptyBox: {
        marginTop: theme.spacing(2),
    },
    contentWrapper: {
        maxWidth: theme.custom.contentAreaWidth,
        paddingLeft: theme.spacing(3),
    },
}));

/**
 * Renders deployments List
 * @class CloudClusters
 * @param {*} props
 * @extends {React.Component}
 */
export default function CloudClusters(props) {
    const classes = useStyles();
    const { api } = useContext(APIContext);
    const { getDeployments, selectedDeployments, setSelectedDeployments } = props;

    console.log('get deployments', getDeployments);
    // console.log('kubernetes envs', selectedDeployments.map((clusters)
    // => clusters.filter((e) => e.type === 'Kubernetes')));
    // eslint-disable-next-line eqeqeq
    console.log('mini envs', selectedDeployments.filter((clusters) => clusters.type == 'minikube'));
    // eslint-disable-next-line eqeqeq
    console.log('k8s envs', selectedDeployments.filter((item) => item.type == 'Kubernetes'));
    console.log('k8s envs with cluster',
        // eslint-disable-next-line eqeqeq
        selectedDeployments.filter((item) => item.type == 'Kubernetes').filter((e) => e.clusterName == 'minikube'));
    // eslint-disable-next-line eqeqeq
    const test1 = selectedDeployments.filter((item) => item.type == 'Kubernetes')
        .filter((e) => e.clusterName.includes('docker-desktop'));
    console.log('test1 array ', test1);
    console.log('test1 array length', test1.length);
    console.log('test1 array length 1', test1.length > 0);
    // console.log(selectedDeployments.filter((item) =>
    // item.type == getDeployments.name).filter((e) => e.clusterName == 'docker-desktop')
    // console.log('get selectedDeployments', selectedDeployments);
    // console.log('get setSelectedDeployments', setSelectedDeployments);

    // const [checked, setChecked] = React.useState(true);

    const state = {
        type: null,
        clusterName: [],
    };
    // console.log('state initial ', state);

    state.type = getDeployments.name;
    // console.log('state after setting type ', state);
    // type: getDeployments.name,
    // state[type] = getDeployments.name;
    // const previousState = this.state[clusterName] || {};
    return (
        <>
            <Typography variant='h4' gutterBottom align='left' className={classes.mainTitle}>
                <FormattedMessage
                    id='Apis.Details.Environments.Environments.CloudClusters'
                    defaultMessage={getDeployments.name}
                />
            </Typography>

            <Paper className={classes.gatewayPaper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell />
                            <TableCell align='left'>Name</TableCell>
                            <TableCell align='left'>Namespace</TableCell>
                            <TableCell align='left'>Master URL</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {getDeployments.clusters.map((row) => (

                            <TableRow key={row.clusterId}>
                                <TableCell padding='checkbox'>
                                    {/* <Checkbox
                                        disabled={isRestricted(['apim:api_create', 'apim:api_publish'], api)}
                                        checked={selectedDeployments.includes(row.clusterId)}
                                        onChange={
                                            (event) => {
                                                console.log('row data', row);
                                                console.log(event.target);
                                                console.log('selected edp 1', selectedDeployments);
                                                const { checked, name } = event.target;
                                                console.log("checked ", checked);
                                                // console.log("name ", name);
                                                if (checked) {
                                                    setSelectedDeployments([...selectedDeployments, name]);
                                                    console.log('select dep if', selectedDeployments);
                                                } else {
                                                    // console.log(row);
                                                    setSelectedDeployments(
                                                        selectedDeployments.filter((env) => env.clusterId !== name),
                                                    );
                                                    // console.log('selected dep else', selectedDeployments);
                                                }
                                            }
                                        }
                                        name={row.clusterId}
                                        color='primary'
                                    /> */}
                                    {console.log('seleceteed deployments', selectedDeployments)}
                                    {console.log(row.clusterId)}
                                    {/* {console.log('logic ', selectedDeployments.filter((item) =>
                                    item.type == getDeployments.name).clusterName.includes(row.clusterId))} */}
                                    {/* {console.log('test', selectedDeployments.map((clusters) =>
                                    clusters.filter((e) => e.clusterName == 'minikube')))} */}
                                    <Checkbox
                                        disabled={isRestricted(['apim:api_create', 'apim:api_publish'], api)}
                                        // checked={selectedDeployments[0].clusterName == row.clusterId}
                                        // checked={selectedDeployments.map((clusters)
                                        //  => clusters.map((e) => e.clusterName.includes(row.clusterId)))}
                                        // checked={selectedDeployments.map((clusters) =>
                                        // clusters.map((e) => e.clusterName.includes(row.clusterId)))}
                                        // checked={selectedDeployments.filter((clusters)
                                        // => clusters.type == 'Kubernetes').length > 0}
                                        // eslint-disable-next-line eqeqeq
                                        checked={selectedDeployments.filter((item) => item.type == getDeployments.name)
                                            .includes(row.clusterId)}
                                        onChange={
                                            (event) => {
                                                const { checked, name } = event.target;
                                                console.log('event target ', event.target);
                                                console.log('checked ', checked);
                                                console.log('name ', name);
                                                if (checked) {
                                                    // if (selectedDeployments.filter((item) =>
                                                    // item.type == getDeployments.name).length > 0) {
                                                    //     const currentItem = selectedDeployments
                                                    // .filter((item) => item.type == getDeployments.name)
                                                    // .clusterName.push(row.clusterId);
                                                    //     setSelectedDeployments(...selectedDeployments, currentItem);
                                                    // }
                                                    selectedDeployments
                                                        // eslint-disable-next-line eqeqeq
                                                        .filter((item) => item.type == getDeployments.name)
                                                        .clusterName.push(name);
                                                    // state.clusterName.push(name);
                                                    // setSelectedDeployments(...selectedDeployments, state);
                                                    setSelectedDeployments({
                                                        ...selectedDeployments
                                                            // eslint-disable-next-line eqeqeq
                                                            .filter((item) => item.type == getDeployments.name),
                                                        clusterName: {
                                                            ...selectedDeployments
                                                                // eslint-disable-next-line eqeqeq
                                                                .filter((item) => item.type == getDeployments.name)
                                                                .clusterName,
                                                            name,
                                                        },
                                                    });
                                                } else {
                                                    setSelectedDeployments(
                                                        // selectedDeployments.clusterName
                                                        // .filter((env) => env.clusterName !== name),
                                                        // selectedDeployments.clusterName.
                                                        // filter((env) => env.clusterName !== name),
                                                        // selectedDeployments.map((clusters) => clusters
                                                        // .filter((e) => e.clusterName !== name)),
                                                        // eslint-disable-next-line eqeqeq
                                                        selectedDeployments.filter((item) => item.type == 'Kubernetes')
                                                            .filter((e) => e.clusterName !== name),
                                                    );
                                                    console.log('selected dep else', selectedDeployments);
                                                }
                                            }
                                        }
                                        name={row.clusterId}
                                        color='primary'
                                    />
                                </TableCell>
                                <TableCell component='th' scope='row'>
                                    {row.clusterName}
                                </TableCell>
                                <TableCell align='left'>{row.namespace}</TableCell>
                                <TableCell align='left'>{row.masterURL}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </Paper>
        </>
    );
}
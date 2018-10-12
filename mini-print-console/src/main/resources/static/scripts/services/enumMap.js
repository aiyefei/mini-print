'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.service:CodeMapService
 * @description # codemap service of the sbAdminApp
 */

angular.module('sbAdminApp').service('enumMapService', function() {

    this.taskStatusEnum = {
        '10': '初始化',
        '20': '待执行',
        '30': '执行中',
        '40': '已执行',
        '41': '执行失败'
    };

    this.printWayEnum = {
        0: '逐页打印',
        1: '逐份打印'
    };

    this.duplexModeEnum = {
        0: '单面',
        1: '双面长边',
        2: '双面短边'
    };

    this.printDirectionEnum = {
        1: '横向',
        2: '纵向',
        3: '自适应'
    };

    this.taskTypeEnum = {
        'P': '打印',
        'S': '扫描'
    };

});

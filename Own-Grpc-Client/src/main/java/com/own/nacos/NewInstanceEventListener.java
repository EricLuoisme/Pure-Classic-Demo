package com.own.nacos;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import io.grpc.stub.AbstractStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class NewInstanceEventListener extends Subscriber<InstancesChangeEvent> {

    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }

    @Override
    public void onEvent(InstancesChangeEvent instancesChangeEvent) {
        log.info(">>> Received Nacos InstancesChangeEvent: {}", JSON.toJSONString(instancesChangeEvent));
        System.out.println();
    }

    @PostConstruct
    public void postConstruct() {
        NotifyCenter.registerSubscriber(this);
    }


}

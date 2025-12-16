package com.soul.fin;

/*
    goal: to demo a simple kafka producer using java functional interfaces
 */

//@Configuration
//public class KafkaProducer {
//
//    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
//
/// /    @Bean
/// /    public SenderOptionsCustomizer customizer(){
/// /        return (s, so) -> so.producerProperty(ProducerConfig.ACKS_CONFIG, "all")
/// /                .producerProperty(ProducerConfig.BATCH_SIZE_CONFIG, "20001");
/// /    }
//
//    @Bean
//    public Supplier<Flux<String>> producer() {
//        return () -> Flux.interval(Duration.ofSeconds(1))
//                .take(10)
//                .map(i -> "msg " + i)
//                .doOnNext(m -> log.info("produced {}", m));
//    }
//
//}

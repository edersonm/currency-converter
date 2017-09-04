package com.currency;

import com.currency.core.CurrencyData;
import com.currency.db.CurrencyDAO;
import com.currency.resources.CurrencyResource;
import com.currency.resources.SchedulerResource;
import com.currency.service.CurrencyProcessorService;
import com.currency.service.RunAssyncRequest;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CurrencyConverterApplication extends Application<CurrencyConverterConfiguration> {

	public static void main(final String[] args) throws Exception {
		new CurrencyConverterApplication().run(args);
	}

	private final HibernateBundle<CurrencyConverterConfiguration> hibernateBundle = new HibernateBundle<CurrencyConverterConfiguration>(
			CurrencyData.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(CurrencyConverterConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	@Override
	public String getName() {
		return "Currency Converter";
	}

	@Override
	public void initialize(final Bootstrap<CurrencyConverterConfiguration> bootstrap) {
		bootstrap.addBundle(hibernateBundle);
	}

	@Override
	public void run(final CurrencyConverterConfiguration configuration, final Environment environment) {
		final CurrencyDAO dao = new CurrencyDAO(hibernateBundle.getSessionFactory());
		// In order to keep the session open and use @UnitOfWork on
		// methods/objects outside Jersey context(on assync call, for example),
		// we have to use UnitOfWorkAwareProxyFactory as described in:
		// http://www.dropwizard.io/1.0.0/docs/manual/hibernate.html#transactional-resource-methods
		final CurrencyProcessorService service = new UnitOfWorkAwareProxyFactory(hibernateBundle)
				.create(CurrencyProcessorService.class, CurrencyDAO.class, dao);

		environment.jersey().register(new CurrencyResource(service));
		environment.jersey().register(new SchedulerResource());

		final RunAssyncRequest assync = new RunAssyncRequest(service);
		assync.checker();
	}

}
